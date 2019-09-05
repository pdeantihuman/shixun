import {NgModule} from "@angular/core";
import {BrowserModule} from '@angular/platform-browser'
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http'
import {CookieService} from "ngx-cookie-service";
import {AppComponent} from "./app.component";
import {JwtInterceptor} from "../_interceptor/jwt-interceptor.service";
import {ErrorInterceptor} from "../_interceptor/error-interceptor.service";
import {LoginComponent} from "../component/login/login.component";
import {CartComponent} from "../component/cart/cart.component";
import {DetailComponent} from "../component/product-detail/detail.component";
import {SignupComponent} from "../component/signup/signup.component";
import {CardComponent} from "../component/card/card.component";
import {OrderDetailComponent} from "../component/order-detail/detail.component";
import {AppRoutingModule} from "./app-routing.module";
import {OrderComponent} from "../component/order/order.component";
import {PaginationComponent} from "../parts/pagination/pagination.component";
import {NavigationComponent} from "../parts/navigation/navigation.component";
import {ProductListComponent} from "../component/product-list/product.component";
import {UserDetailComponent} from "../component/user-detail/detail.component";
import {ProductEditComponent} from "../component/product-edit/edit.component";
import {FormsModule} from "@angular/forms";

@NgModule({
    declarations: [
        AppComponent,
        NavigationComponent,
        CardComponent,
        PaginationComponent,
        LoginComponent,
        SignupComponent,
        DetailComponent,
        CartComponent,
        OrderComponent,
        OrderDetailComponent,
        ProductListComponent,
        UserDetailComponent,
        ProductEditComponent,

    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,

    ],
    providers: [CookieService,
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
    bootstrap: [AppComponent]
})
export class AppModule {
}