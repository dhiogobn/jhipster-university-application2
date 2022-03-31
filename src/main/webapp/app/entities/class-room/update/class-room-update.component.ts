import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IClassRoom, ClassRoom } from '../class-room.model';
import { ClassRoomService } from '../service/class-room.service';

@Component({
  selector: 'jhi-class-room-update',
  templateUrl: './class-room-update.component.html',
})
export class ClassRoomUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    number: [],
    capacity: [],
  });

  constructor(protected classRoomService: ClassRoomService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classRoom }) => {
      this.updateForm(classRoom);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classRoom = this.createFromForm();
    if (classRoom.id !== undefined) {
      this.subscribeToSaveResponse(this.classRoomService.update(classRoom));
    } else {
      this.subscribeToSaveResponse(this.classRoomService.create(classRoom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassRoom>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(classRoom: IClassRoom): void {
    this.editForm.patchValue({
      id: classRoom.id,
      number: classRoom.number,
      capacity: classRoom.capacity,
    });
  }

  protected createFromForm(): IClassRoom {
    return {
      ...new ClassRoom(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      capacity: this.editForm.get(['capacity'])!.value,
    };
  }
}
