import {BehaviorSubject, Observable, of} from "rxjs";
import {ProductInOrder} from "../model/ProductInOrder";
import {catchError, map, tap} from "rxjs/operators";
import {UserService} from "./user.service";
import {CookieService} from "ngx-cookie-service";
import {HttpClient} from "@angular/common/http";
import {JwtResponse} from "../response/JwtResponse";
import {Injectable} from "@angular/core";
import {apiUrl} from "../environments/environment";
import {Cart} from "../model/cart";
import {Item} from "../model/Item";

@Injectable({
    providedIn: 'root'
})
export class CartService {


    private cartUrl = `${apiUrl}/cart`;

    /**
     * 存储购物车信息
     */
    localMap = {};
    private itemsSubject: BehaviorSubject<Item[]>;
    private totalSubject: BehaviorSubject<number>;
    public items: Observable<Item[]>;
    public total: Observable<number>;


    private currentUser: JwtResponse;

    constructor(private http: HttpClient,
                private cookieService: CookieService,
                private userService: UserService) {
        this.itemsSubject = new BehaviorSubject<Item[]>(null);
        this.items = this.itemsSubject.asObservable();
        this.totalSubject = new BehaviorSubject<number>(null);
        this.total = this.totalSubject.asObservable();
        this.userService.currentUser.subscribe(user => this.currentUser = user);


    }

    /**
     * 从 Cookie 中提取购物车列表
     */
    private getLocalCart(): ProductInOrder[] {
        if (this.cookieService.check('cart')) {
            this.localMap = JSON.parse(this.cookieService.get('cart'));
            return Object.values(this.localMap);
        } else {
            this.localMap = {};
            return [];
        }
    }

    // 从服务端下载购物车
    getCart(): Observable<ProductInOrder[]> {
        const localCart = this.getLocalCart();
        // 如果已登陆，从服务器上下载 cart
        if (this.currentUser) {
            if (localCart.length > 0) {
                return this.http.post<Cart>(this.cartUrl, localCart).pipe<any, any, any>(
                    tap(_ => {
                        this.clearLocalCart();
                    }),
                    map(cart => cart.products),
                    catchError(_ => of([]))
                );
            } else {
                return this.http.get<Cart>(this.cartUrl).pipe<any, any>(
                    map(cart => cart.products),
                    catchError(_ => of([]))
                );
            }
        } else {
            // 返回 of(localCart)
            return of(localCart);
        }
    }

    /**
     * 添加product到购物车
     * @param productInOrder
     */
    addItem(productInOrder: ProductInOrder): Observable<boolean> {
        // 若未登陆，写入 cookie 本地存储
        // 若已登陆，上传到服务端
        if (!this.currentUser) {
            if (this.cookieService.check('cart')) {
                this.localMap = JSON.parse(this.cookieService.get('cart'));
            }
            if (!this.localMap[productInOrder.productId]) {
                this.localMap[productInOrder.productId] = productInOrder;
            } else {
                this.localMap[productInOrder.productId].count += productInOrder.count;
            }
            this.cookieService.set('cart', JSON.stringify(this.localMap));
            return of(true);
        } else {
            const url = `${this.cartUrl}/add`;
            return this.http.post<boolean>(url, {
                'quantity': productInOrder.count,
                'productId': productInOrder.productId
            });
        }
    }

    /**
     * 更新一个产品在购物车中的数量到服务端
     * @param productInOrder
     */
    update(productInOrder: ProductInOrder): Observable<ProductInOrder> {

        if (this.currentUser) {
            const url = `${this.cartUrl}/${productInOrder.productId}`;
            return this.http.put<ProductInOrder>(url, productInOrder.count);
        }
    }


    /**
     * 将一个产品从购物车中移除
     * @param productInOrder
     */
    remove(productInOrder: ProductInOrder) {
        if (!this.currentUser) {
            delete this.localMap[productInOrder.productId];
            return of(null);
        } else {
            const url = `${this.cartUrl}/${productInOrder.productId}`;
            return this.http.delete(url).pipe( );
        }
    }


    /**
     * 开启结算
     */
    checkout(): Observable<any> {
        const url = `${this.cartUrl}/checkout`;
        return this.http.post(url, null).pipe();
    }

    /**
     * 保存当前购物车到本地 cookie
     */
    storeLocalCart() {
        this.cookieService.set('cart', JSON.stringify(this.localMap));
    }

    clearLocalCart() {
        console.log('clear local cart');
        this.cookieService.delete('cart');
        this.localMap = {};
    }

}
