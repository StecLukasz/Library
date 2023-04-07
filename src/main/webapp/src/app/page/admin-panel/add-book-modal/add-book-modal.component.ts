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
    console.log(this.bookDTO);
    this.addBook();
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
}
