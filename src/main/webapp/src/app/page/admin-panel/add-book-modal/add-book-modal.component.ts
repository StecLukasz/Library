import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../../api.service';

@Component({
  selector: 'app-add-book-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-book-modal.component.html',
  styleUrls: ['./add-book-modal.component.scss'],
})
export class AddBookModalComponent {
  book: any = {};

  constructor(private api: ApiService, private router: Router) {}

  addBook() {
    this.api.addBookAdmin(this.book).subscribe(
      (data) => {
        console.log(data);
        this.goToAdminPanel();
      },
      (error) => console.log(error)
    );
  }

  goToAdminPanel() {
    this.router.navigate(['/admin-panel']);
  }

  onSubmit() {
    console.log(this.book);
    this.addBook();
  }
}
