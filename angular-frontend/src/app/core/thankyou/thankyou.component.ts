import { Component } from '@angular/core';
import {_client_order} from "../../shared/_constVars/_client_consts";
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-thankyou',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './thankyou.component.html',
  styleUrl: './thankyou.component.scss'
})
export class ThankyouComponent {
  order: string = `/${_client_order}/`;
}
