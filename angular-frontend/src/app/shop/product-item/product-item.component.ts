import {Component, inject, Input, OnInit} from '@angular/core';
import {_client_shop} from "../../shared/_constVars/_client_consts";
import {IProduct} from "../../shared/_models/IProduct";
import {Observable} from "rxjs";
import {ICart, ICartItem} from "../../shared/_models/ICart";
import {CartService} from "../../core/_services/cart.service";
import {_checkItemExistsInCart} from "../../shared/_helper/_checkItemExistsInCart";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-product-item',
    standalone: true,
    imports: [RouterModule, CommonModule],
    templateUrl: './product-item.component.html',
    styleUrl: './product-item.component.scss'
})
export class ProductItemComponent implements OnInit {
    #cartService = inject(CartService);
    shop: string = _client_shop;
    @Input() product: IProduct;
    isContains: boolean = false;
    cart$: Observable<ICart>;

    constructor() {
        this.cart$ = this.#cartService.cart$;
    }

    ngOnInit(): void {
        this.cart$.subscribe({
            next: e => {
                if (e) {
                    this.checkItemExistsInCart(e.cartItems);
                } else {
                    this.isContains = false;
                }
            },
            error: err => console.error(err)
        });
    }

    checkItemExistsInCart(c: ICartItem[]) {
        this.isContains = c && _checkItemExistsInCart(c, this.product.id);
    }

    addItemToCart(id: string) {
        this.#cartService.editCart({
            productId: id,
            quantity: 1
        }).subscribe({
            next: (e: ICartItem[]) => this.checkItemExistsInCart(e),
            error: err => console.error(err)
        });
    }

    removeItemFromCart(id: string) {
        this.#cartService.removeItemFromCart(id)
            .subscribe({
                next: (e: ICartItem[]) => this.checkItemExistsInCart(e),
                error: err => console.error(err)
            });
    }
}
