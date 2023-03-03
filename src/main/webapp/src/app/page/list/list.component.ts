import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { Book } from '../../shared/interface/book';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {
  books: Book[] = [];

  constructor(private api: ApiService) {}

  async ngOnInit(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }
}
