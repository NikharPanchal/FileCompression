import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileServiceService {

  baseUrl ="http://localhost:8080/api/file"

  constructor(private http:HttpClient) { }

  uploadFile(formData:FormData){
    return this.http.post("http://localhost:8080/api/file/upload", formData)
  }

  downloadFile(fileId:any) {
    const url = this.baseUrl + "/download/" + fileId;
    if (!this.http) {
      throw new Error("this.http is null");
    }
    if (!url) {
      throw new Error("url is null");
    }
    return this.http.get(url, {
      observe: 'response',
      responseType: 'blob',
      headers: new HttpHeaders().append('Content-Type', 'application/json') || null,
    }).pipe(
      catchError(error => {
        console.error("downloadFile: ", error);
        throw new Error("downloadFile: " + error.message);
      })
    );
  }
}