import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AppInfo } from '../../shared/interface/app-info';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {
  year = new Date().getFullYear();
  version = '0.0.0';
  appInfo?: AppInfo;

  constructor(private api: ApiService) {}

  // ngOnInit(): void {
  //   if (this.api.appInfo) {
  //     this.appInfo = this.api.appInfo;
  //   } else {
  //     this.api.getAppInfo().subscribe((data) => {
  //       this.appInfo = data;
  //     });
  //   }
  // }

  async ngOnInit(): Promise<void> {
    if (this.api.appInfo) {
      this.appInfo = this.api.appInfo;
    } else {
      this.appInfo = await firstValueFrom(this.api.getAppInfo());
    }
  }
}
