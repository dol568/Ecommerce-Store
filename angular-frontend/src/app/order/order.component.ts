import {Component, inject, OnInit} from '@angular/core';
import {IOrder} from "../shared/_models/IOrder";
import {OrderService} from "../core/_services/order.service";
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit {
  #orderService = inject(OrderService);
  orders: IOrder[];

  constructor() {
  }

  ngOnInit(): void {
    this.getOrders();
  }

  getOrders() {
    this.#orderService.getOrders().subscribe({
      next: value => this.orders = value,
      error: err => console.error(err)
    });
  }
}
