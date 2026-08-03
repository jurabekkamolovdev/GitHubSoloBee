import { ExecutionContext, Injectable } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { AuthGuard } from '@nestjs/passport';
import { IS_PUBLIC_KEY } from 'src/core/decorators/public.decorator';

export const IS_REFRESH_KEY = 'isRefresh';

@Injectable()
export class JwtAuthGuard extends AuthGuard('jwt') {
  constructor(private reflector: Reflector) {
    super();
  }

  canActivate(context: ExecutionContext) {
    const isPublic = this.reflector.getAllAndOverride<boolean>(IS_PUBLIC_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);

    if (isPublic) return true;

    const isRefresh = this.reflector.getAllAndOverride<boolean>(
      IS_REFRESH_KEY,
      [context.getHandler(), context.getClass()],
    );
    if (isRefresh) return true;

    // const request = context.switchToHttp().getRequest();
    // console.log('AUTH HEADER:', request.headers.authorization);

    return super.canActivate(context);
  }

  // handleRequest(err: any, user: any, info: any) {
  //   console.log('JWT USER:', user);
  //   console.log('JWT INFO:', info); // ← xato sababi shu yerda
  //   console.log('JWT ERR:', err);
  //   if (err || !user) throw err || new UnauthorizedException();
  //   return user;
  // }
}

@Injectable()
export class JwtRefreshGuard extends AuthGuard('jwt-refresh') {}
