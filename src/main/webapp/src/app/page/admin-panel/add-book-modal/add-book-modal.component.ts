import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../../api.service';
import { BookDTO } from '../../../shared/interface/bookDTO';

@Component({
  selector: 'app-add-book-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-book-modal.component.html',
  styleUrls: ['./add-book-modal.component.scss'],
})
export class AddBookModalComponent {
  book: any = {};
  bookDTO: BookDTO = {
    title: '',
    genre: '',
    pages: 0,
    adminSignatureDTO: [{ bookSignature: '' }],
    authorDTO: [{ firstName: '', lastName: '', gender: '', birthDate: new Date() }],
  };

  constructor(private api: ApiService, private router: Router) {}

  addBook() {
    this.api.addBookAdmin(this.bookDTO).subscribe(
      (data) => {
        console.log(data);
      },
      (error) => console.log(error)
    );
  }

  goToAdminPanel() {
    this.router.navigate(['/admin-panel']);
  }

  onSubmit() {
    if (this.validateForm()) {
      console.log(this.bookDTO);
      this.addBook();
      this.goToAdminPanel();
    }
  }

  addAuthor(): void {
    this.bookDTO.authorDTO.push({
      firstName: '',
      lastName: '',
      gender: '',
      birthDate: new Date(),
    });
  }

  addSignature(): void {
    this.bookDTO.adminSignatureDTO.push({
      bookSignature: '',
    });
  }

  validateForm(): boolean {
    if (!this.bookDTO.title || !this.bookDTO.pages || !this.bookDTO.genre) {
      alert('Please fill in all required fields.');
      return false;
    }
    for (const author of this.bookDTO.authorDTO) {
      if (!author.firstName || !author.lastName) {
        alert('Please fill in all required fields for each author.');
        return false;
      }
    }
    for (const signature of this.bookDTO.adminSignatureDTO) {
      if (!signature.bookSignature) {
        alert('Please fill in all required fields for each signature.');
        return false;
      }
    }
    return true;
  }
}
