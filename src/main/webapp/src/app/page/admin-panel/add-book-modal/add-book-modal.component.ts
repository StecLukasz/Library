import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-book-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-book-modal.component.html',
  styleUrls: ['./add-book-modal.component.scss'],
})
export class AddBookModalComponent {
  book: any = {};

  constructor() {}

  onSubmit() {
    console.log(this.book);
  }

  // showDialog() {
  //   this.displayDialog = true;
  // }
}
