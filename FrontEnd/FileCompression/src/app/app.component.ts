import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FileServiceService } from './service/file-service.service';
import { response } from 'express';
import JSZip from 'jszip';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent{
  constructor(private service:FileServiceService){}

  selectedFile: File | null = null;
  fileId: string | null = null;

  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onSubmit(event: Event) {
    if (this.selectedFile) {
      event.preventDefault();
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      this.service.uploadFile(formData).subscribe((response: any) => {
        this.fileId = response.responseId;
        console.log("Response ID:", this.fileId);
      }, (error: any) => {
        console.error("Error occurred:", error);
      });
    } else {
      console.error("File is null");
    }
  }

  downloadFile() {
    console.log(this.fileId);
    this.service.downloadFile(this.fileId).subscribe(response => {
      if (response.body) {
        const contentType = response.headers.get('Content-Type') ?? 'application/octet-stream';
        const blob = new Blob([response.body], { type: contentType });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'download.zip');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else {
        console.error('Error: Response body is null');
      }
    });
  }
  
}
