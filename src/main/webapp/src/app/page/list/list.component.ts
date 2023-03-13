import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { Book } from '../../shared/interface/book';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {
  books: Book[] = [];
  search: string = '';

  constructor(private api: ApiService) {}

  async ngOnInit(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  onSubmit() {
    this.getSentencesSearch(this.search);
    console.log(this.search);
  }

  private getSentencesSearch(text: string) {
    this.api.getBooksSearch(text).subscribe((data) => {
      this.books = data;
    });
  }
}
