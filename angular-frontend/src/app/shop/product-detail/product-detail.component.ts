import {Component, inject, OnInit} from '@angular/core';
import {IProduct} from "../../shared/_models/IProduct";
import {CartService} from "../../core/_services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductsService} from "../../core/_services/products.service";
import {_client_checkout} from "../../shared/_constVars/_client_consts";
import {_findItemExistsInCart} from "../../shared/_helper/_checkItemExistsInCart";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-product-detail',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './product-detail.component.html',
    styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
    #cartService = inject(CartService);
    #activatedRoute = inject(ActivatedRoute);
    #productService = inject(ProductsService);
    #router = inject(Router);
    product: IProduct;
    quantity = 1;

    constructor() {
    }

    ngOnInit(): void {
        this.loadProduct();
    }

    addItemToCart(id: string) {
        this.#cartService.editCart({productId: id, quantity: this.quantity})
            .subscribe({
                next: (res) => {
                    this.#router.navigate([`/${_client_checkout}/`]);
                },
                error: (err) => console.error(err)
            });
    }

    incrementQuantity() {
        this.quantity++;
    }

    decrementQuantity() {
        if (this.quantity > 1) this.quantity--;
    }

    checkProduct() {
        const cart = this.#cartService.getCurrentCartValue().cartItems;
        const item = _findItemExistsInCart(cart, this.product.id);
        if (item) {
            this.quantity = item.quantity;
        }
    }

    loadProduct() {
        this.#productService.product$(this.#activatedRoute.snapshot.paramMap.get('id')).subscribe({
            next: (product) => {
                this.product = product;
                this.checkProduct();
            },
            error: (err) => console.log(err)
        });
    }
}
