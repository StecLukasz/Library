import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-add-book-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './add-book-modal.component.html',
  styleUrls: ['./add-book-modal.component.scss'],
})
export class AddBookModalComponent {
  constructor(public dialogRef: MatDialogRef<AddBookModalComponent>) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
