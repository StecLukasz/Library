import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AppInfo } from '../../shared/interface/app-info';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss'],
})
export class AboutComponent implements OnInit {
  appInfo?: AppInfo;

  constructor(private api: ApiService) {}

  async ngOnInit(): Promise<void> {
    if (this.api.appInfo) {
      this.appInfo = this.api.appInfo;
    } else {
      this.appInfo = await firstValueFrom(this.api.getAppInfo());
    }
  }
}
