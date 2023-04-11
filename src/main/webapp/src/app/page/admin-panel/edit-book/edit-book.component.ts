import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../../api.service';
import { BookDTO } from '../../../shared/interface/bookDTO';

@Component({
  selector: 'app-edit-book',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './edit-book.component.html',
  styleUrls: ['./edit-book.component.scss'],
})
export class EditBookComponent {
  bookDTO: BookDTO = {
    title: '',
    genre: '',
    pages: 0,
    adminSignatureDTO: [{ bookSignature: '' }],
    authorDTO: [{ firstName: '', lastName: '', gender: '', birthDate: new Date() }],
  };

  constructor(private api: ApiService, private router: Router) {}

  addBook() {
    // tutaj też dać edit
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
    // this.addBook();
    // tutaj edit dac
  }
}
