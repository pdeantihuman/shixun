import {Component, OnInit} from "@angular/core";
import {User} from "../../model/user";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {Location} from "@angular/common";

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

    user: User;

    constructor( private location: Location,
                 private userService: UserService,
                 private router: Router) {
        this.user = new User();

    }



    ngOnInit() {


    }
    onSubmit() {
        this.userService.signUp(this.user).subscribe(u => {
                this.router.navigate(['/login']);
            },
            e => {});
    }

}