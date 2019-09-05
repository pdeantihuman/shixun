import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {OrderService} from "../../service/order.service";
import {Order} from "../../model/order";

@Component({
    selector: 'app-order-detail',
    templateUrl: './order-detail.component.html',
    styleUrls: ['./order-detail.component.css']
})
export class OrderDetailComponent implements OnInit {

    constructor(private orderService: OrderService,
                private route: ActivatedRoute) {
    }

    order$: Observable<Order>;

    ngOnInit() {
        // this.items$ = this.route.paramMap.pipe(
        //     map(paramMap =>paramMap.get('id')),
        //     switchMap((id:string) => this.orderService.show(id))
        // )
        this.order$ = this.orderService.show(this.route.snapshot.paramMap.get('id'));
    }

}