import {Component, Input} from '@angular/core';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-paging-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './paging-header.component.html'
})
export class PagingHeaderComponent {
  @Input() totalElements: number;
  @Input() pageSize: number;
  @Input() pageNumber: number;
}
