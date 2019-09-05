import {Injectable} from "@angular/core";
import {apiUrl} from "../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {catchError} from "rxjs/operators";
import {Order} from "../model/order";

@Injectable({
    providedIn: 'root'
})
export class OrderService {

    private orderUrl = `${apiUrl}/order`;

    constructor(private http: HttpClient) {
    }

    getPage(page = 1, size = 10): Observable<any> {
        return this.http.get(`${this.orderUrl}?page=${page}&size=${size}`).pipe();
    }

    show(id): Observable<Order> {
        return this.http.get<Order>(`${this.orderUrl}/${id}`).pipe<any>(
            catchError(_ => of(null))
        );
    }

    cancel(id): Observable<Order> {
        return this.http.patch<Order>(`${this.orderUrl}/cancel/${id}`, null).pipe<any>(
            catchError(_ => of(null))
        );
    }

    finish(id): Observable<Order> {
        return this.http.patch<Order>(`${this.orderUrl}/finish/${id}`, null).pipe<any>(
            catchError(_ => of(null))
        );
    }
}