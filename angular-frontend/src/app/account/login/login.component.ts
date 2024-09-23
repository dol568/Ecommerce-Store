import {Component, inject, OnInit} from '@angular/core';
import {_client_home} from "../../shared/_constVars/_client_consts";
import {ActivatedRoute, Router} from "@angular/router";
import {AccountService} from "../../core/_services/account.service";
import {CartService} from "../../core/_services/cart.service";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [FormsModule, CommonModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
    #router = inject(Router);
    #activatedRoute = inject(ActivatedRoute);
    #accountService = inject(AccountService);
    #cartService = inject(CartService);

    returnUrl: string;
    readonly emailOnly = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;


    ngOnInit(): void {
        this.returnUrl = this.#activatedRoute.snapshot.queryParams['returnUrl'];
    }

    isValidateTextTrue(data: any) {
        if (data.touched && data.valid) {
            return true;
        } else {
            return false;
        }
    }

    isValidateTextFalse(data: any) {
        if (data.touched && data.invalid) {
            return true;
        } else {
            return false;
        }
    }

    submitFunc(data: any, event: Event) {
        event.preventDefault();
        this.#accountService.login(data.value).subscribe({
            next: () => {
                this.#router.navigate([_client_home]);
                this.loadCart();
            },
            error: err => console.error(err)
        });
    }

    loadCart() {
        this.#cartService.getCart().subscribe({
            error: err => console.error(err)
        });
    }
}
