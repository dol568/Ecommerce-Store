import { Component } from '@angular/core';
import {_client_home} from "../../shared/_constVars/_client_consts";
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-server-error',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './server-error.component.html',
  styleUrl: './server-error.component.scss'
})
export class ServerErrorComponent {
  home: string = _client_home;
}
