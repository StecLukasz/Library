import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../../api.service';
import { BookDTO } from '../../../shared/interface/bookDTO';

@Component({
  selector: 'app-edit-book',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-book.component.html',
  styleUrls: ['./edit-book.component.scss'],
})
export class EditBookComponent implements OnInit {
  bookDTO: BookDTO = {
    bookId: 0,
    title: '',
    genre: '',
    pages: 0,
    adminSignatureDTO: [{ bookSignature: '' }],
    authorDTO: [{ firstName: '', lastName: '', gender: '', birthDate: new Date() }],
  };
  bookId: number = 0;

  constructor(private route: ActivatedRoute, private router: Router, private api: ApiService) {}

  ngOnInit() {
    this.bookId = this.route.snapshot.params['bookId'];
    this.api.getBookForAdmin(this.bookId).subscribe(
      (data) => {
        this.bookDTO = data;
      },
      (error) => console.log(error)
    );
  }

  onSubmit() {
    this.api.editBookAdmin(this.bookId, this.bookDTO).subscribe(
      (data) => {
        this.goToAdminPanel();
      },
      (error) => console.log(error)
    );
  }

  goToAdminPanel() {
    this.router.navigate(['/admin-panel']);
  }
}
