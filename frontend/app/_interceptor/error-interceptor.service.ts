import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {UserService} from "../service/user.service";
import {Router} from "@angular/router";

@Injectable({providedIn: 'root'})
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private userService: UserService, private router: Router) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe<HttpEvent<any>>(
            catchError<any, any>(err => {
                if (err.status == 401) {
                    this.userService.logout();
                    this.router.navigate(['/login'])
                }
                const error = err.error || err.statusText;
                return throwError(error)
            }))
    }
}
