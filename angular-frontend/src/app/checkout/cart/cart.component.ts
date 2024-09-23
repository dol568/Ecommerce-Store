import {Component, inject, OnInit} from '@angular/core';
import {CartService} from "../../core/_services/cart.service";
import {ICart} from "../../shared/_models/ICart";
import {Observable} from "rxjs";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-cart',
    standalone: true,
    imports: [RouterModule, CommonModule],
    templateUrl: './cart.component.html',
    styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {
    #cartService = inject(CartService);
    cart$: Observable<ICart>;

    constructor() {
    }

    ngOnInit(): void {
        this.cart$ = this.#cartService.cart$;
    }

    deleteCartItem(id: string) {
        this.#cartService.removeItemFromCart(id).subscribe({
                error: err => console.error(err)
            });
    }
}
