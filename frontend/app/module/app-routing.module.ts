import {NgModule} from "@angular/core";
import {Role} from "../enum/Role";
import {RouterModule, Routes} from "@angular/router";
import {CardComponent} from "../component/card/card.component";
import {LoginComponent} from "../component/login/login.component";
import {SignupComponent} from "../component/signup/signup.component";
import {CartComponent} from "../component/cart/cart.component";
import {DetailComponent} from "../component/product-detail/detail.component";
import {OrderDetailComponent} from "../component/order-detail/detail.component";
import {OrderComponent} from "../component/order/order.component";
import {ProductListComponent} from "../component/product-list/product.component";
import {AuthGuard} from "../_guards/auth";
import {UserDetailComponent} from "../component/user-detail/detail.component";
import {ProductEditComponent} from "../component/product-edit/edit.component";

const routes: Routes = [
    {path: '', redirectTo: '/product', pathMatch: 'full'},
    {path: 'product/:id', component: DetailComponent},
    {path: 'category/:id', component: CardComponent},
    {path: 'product', component: CardComponent},
    {path: 'category', component: CardComponent},
    {path: 'login', component: LoginComponent},
    {path: 'logout', component: LoginComponent},
    {path: 'register', component: SignupComponent},
    {path: 'cart', component: CartComponent},
    {path: 'success', component: SignupComponent},
    {path: 'order/:id', component: OrderDetailComponent, canActivate: [AuthGuard]},
    {path: 'order', component: OrderComponent, canActivate: [AuthGuard]},
    {path: 'seller', redirectTo: 'seller/product', pathMatch: 'full'},
    {
        path: 'seller/product',
        component: ProductListComponent,
        canActivate: [AuthGuard],
        data: {roles: [Role.Manager, Role.Employee]}
    },
    {
        path: 'profile',
        component: UserDetailComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'seller/product/:id/edit',
        component: ProductEditComponent,
        canActivate: [AuthGuard],
        data: {roles: [Role.Manager, Role.Employee]}
    },
    {
        path: 'seller/product/:id/new',
        component: ProductEditComponent,
        canActivate: [AuthGuard],
        data: {roles: [Role.Employee]}
    },

];

@NgModule({
    declarations: [],
    imports: [
        RouterModule.forRoot(routes)//{onSameUrlNavigation: 'reload'}
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}