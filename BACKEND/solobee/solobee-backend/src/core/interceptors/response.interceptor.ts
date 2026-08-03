import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ObjectResponse } from '../utils/base-response';

@Injectable()
export class ResponseInterceptor<T> implements NestInterceptor<
  T,
  ObjectResponse<T>
> {
  intercept(
    context: ExecutionContext,
    next: CallHandler<T>,
  ): Observable<ObjectResponse<T>> {
    return next.handle().pipe(
      map((data): ObjectResponse<T> => {
        if (data && typeof data === 'object' && 'status' in data) {
          return data as unknown as ObjectResponse<T>;
        }
        return new ObjectResponse(data);
      }),
    );
  }
}
