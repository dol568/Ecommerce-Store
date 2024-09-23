import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
import {IAddress} from "../../shared/_models/IAddress";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {ICart} from "../../shared/_models/ICart";
import {AccountService} from "../../core/_services/account.service";
import {CartService} from "../../core/_services/cart.service";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-address',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './address.component.html',
    styleUrl: './address.component.scss'
})
export class AddressComponent implements OnInit {
    #accountService = inject(AccountService);
    #cartService = inject(CartService);
    @Output() finishOrder: EventEmitter<IAddress> = new EventEmitter<IAddress>();
    AddressForm: FormGroup;
    cart$: Observable<ICart>;

    constructor() {
        this.setupForm({
            name: '',
            street: '',
            city: '',
            province: '',
            postalCode: '',
            country: ''
        });
        this.getUserAddress();
    }

    ngOnInit(): void {
        this.cart$ = this.#cartService.cart$;
    }

    setupForm(address: IAddress | null) {
        this.AddressForm = new FormGroup({
            name: new FormControl(address.name ?? '', [Validators.required]),
            street: new FormControl(address.street ?? '', [Validators.required]),
            city: new FormControl(address.city ?? '', [Validators.required]),
            province: new FormControl(address.province ?? '', [Validators.required]),
            postalCode: new FormControl(address.postalCode ?? '', [Validators.required]),
            country: new FormControl(address.country ?? '', [Validators.required])
        })
    }

    getUserAddress() {
        this.#accountService.address$().subscribe({
            next: (value) => this.setupForm(value),
            error: err => console.error(err)
        });
    }

    submitFunc() {
        this.finishOrder.emit(this.AddressForm.value);
    }
}
