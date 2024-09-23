import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {AccountService} from "./core/_services/account.service";
import {CartService} from "./core/_services/cart.service";
import {NgxSpinnerModule} from "ngx-spinner";
import {NavbarComponent} from "./core/navbar/navbar.component";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, NgxSpinnerModule, NavbarComponent],
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
    #accountService = inject(AccountService);
    #cartService = inject(CartService);

    constructor() {
    }

    ngOnInit(): void {
        this.loadCurrentUser();
    }

    private loadCurrentUser() {
        const token = localStorage.getItem('token');
        if (token) {
            this.#accountService.loadCurrentUser(token).subscribe({
                next: () => this.loadCart(),
                error: err => console.error(err)
            });
        }
    }

    private loadCart() {
        this.#cartService.getCart().subscribe({
            error: (err) => console.error(err)
        });
    }
}
