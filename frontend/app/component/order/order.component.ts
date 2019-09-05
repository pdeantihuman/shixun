import {Order} from "../../model/order";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../service/user.service";
import {OrderService} from "../../service/order.service";
import {HttpClient} from "@angular/common/http";
import {JwtResponse} from "../../response/JwtResponse";
import {Component, OnDestroy, OnInit} from "@angular/core";
import {OrderStatus} from "../../enum/OrderStatus";
import {Role} from "../../enum/Role";

@Component({
    selector: 'app-order',
    templateUrl: './order.component.html',
    styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit, OnDestroy {

    page: any;
    OrderStatus = OrderStatus;
    currentUser: JwtResponse;
    Role = Role;
    constructor(private httpClient: HttpClient,
                private orderService: OrderService,
                private userService: UserService,
                private route: ActivatedRoute
    ) {
    }

    querySub: Subscription;

    ngOnInit() {
        this.currentUser = this.userService.currentUserValue;
        this.querySub = this.route.queryParams.subscribe(() => {
            this.update();
        });

    }

    update() {
        let nextPage = 1;
        let size = 10;
        if (this.route.snapshot.queryParamMap.get('page')) {
            nextPage = +this.route.snapshot.queryParamMap.get('page');
            size = +this.route.snapshot.queryParamMap.get('size');
        }
        this.orderService.getPage(nextPage, size).subscribe(page => this.page = page, _ => {
            console.log("Get Orde Failed")
        });
    }


    cancel(order: Order) {
        this.orderService.cancel(order.orderId).subscribe(res => {
            if (res) {
                order.orderStatus = res.orderStatus;
            }
        });
    }

    finish(order: Order) {
        this.orderService.finish(order.orderId).subscribe(res => {
            if (res) {
                order.orderStatus = res.orderStatus;
            }
        })
    }

    ngOnDestroy(): void {
        this.querySub.unsubscribe();
    }

}
