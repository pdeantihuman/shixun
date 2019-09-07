import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, of, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {apiUrl} from "../environments/environment";
import {catchError, tap} from "rxjs/operators";
import {JwtResponse} from "../response/JwtResponse";
import {User} from "../model/user";

/**
 * @author john
 */
@Injectable({providedIn: 'root'})
export class UserService {
    private currentUserSubject: BehaviorSubject<JwtResponse>;
    public currentUser: Observable<JwtResponse>;
    public nameTerms = new Subject<string>();
    public name$ = this.nameTerms.asObservable();

    constructor(private http: HttpClient, private cookieService: CookieService) {
        const memo = localStorage.getItem("currentUser");
        this.currentUserSubject = new BehaviorSubject<JwtResponse>(JSON.parse(memo));
        this.currentUser = this.currentUserSubject.asObservable();
        cookieService.set('currentService', memo)
    }

    get currentUserValue() {
        return this.currentUserSubject.value;
    }

    login(loginForm): Observable<JwtResponse> {
        const url = `${apiUrl}/login`;
        return this.http.post<JwtResponse>(url, loginForm).pipe<JwtResponse, any>(
            tap((user: JwtResponse): JwtResponse => {
                if (user && user.token) {
                    this.cookieService.set('currentUser', JSON.stringify(user));
                    if (loginForm.remembered) {
                        localStorage.setItem('currentUser', JSON.stringify(user))
                    }
                    console.log(user.name);
                    this.nameTerms.next(user.name);
                    this.currentUserSubject.next(user);
                    return user;
                }
            }),
            catchError<any, any>(this.handleError('Login Failed', null))
        );
    }

    logout() {
        this.currentUserSubject.next(null)
        localStorage.removeItem('currentUser')
        this.cookieService.delete('currentUser')
    }

    signUp(user: User): Observable<User> {
        const url = `${apiUrl}/register`;
        return this.http.post<User>(url, user)
    }

    update(user: User): Observable<User> {
        const url = `${apiUrl}/profile`
        return this.http.put<User>(url, user);
    }

    get(email: string): Observable<User> {
        const url = `${apiUrl}/profile/${email}`
        return this.http.get<User>(url);
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.log(error)
            return of(result as T);
        }
    }
}
