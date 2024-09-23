import {Component, inject, OnInit} from '@angular/core';
import {OrderService} from "../core/_services/order.service";
import {CartService} from "../core/_services/cart.service";
import {Router} from "@angular/router";
import {IAddress} from "../shared/_models/IAddress";
import {_client_thankyou} from "../shared/_constVars/_client_consts";
import {AddressComponent} from "./address/address.component";
import {CartComponent} from "./cart/cart.component";

@Component({
    selector: 'app-checkout',
    standalone: true,
    imports: [AddressComponent, CartComponent],
    templateUrl: './checkout.component.html',
    styleUrl: './checkout.component.scss'
})
export class CheckoutComponent implements OnInit {
    #orderService = inject(OrderService);
    #cartService = inject(CartService);
    #router = inject(Router);

    constructor() {
    }

    ngOnInit(): void {
    }

    submitOrder(address: IAddress) {
        this.#orderService.addOrder(address).subscribe({
            next: () => this.#cartService.clearCart().subscribe({
                next: () => this.#router.navigate([`/${_client_thankyou}`]),
                error: err => console.error(err)
            }),
            error: err => console.error(err)
        });
    }
}
