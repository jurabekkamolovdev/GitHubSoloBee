import { Injectable } from '@nestjs/common';
import {
  CategoryArrayResponseDto,
  SubCategoryArrayResponseDto,
  TopicArrayResponseDto,
  TopicActivitiesResponseDto,
  CategoryResponseDto,
  SubCategoryResponseDto,
  TopicResponseDto,
  ActivityTabDto,
} from '../model/response/courses-response.dto';

@Injectable()
export class CoursesWebMapper {
  toCategoryArrayResponse(
    data: CategoryResponseDto[],
  ): CategoryArrayResponseDto {
    const response = new CategoryArrayResponseDto();
    response.data = data;
    return response;
  }

  toSubCategoryArrayResponse(
    data: SubCategoryResponseDto[],
  ): SubCategoryArrayResponseDto {
    const response = new SubCategoryArrayResponseDto();
    response.data = data;
    return response;
  }

  toTopicArrayResponse(data: TopicResponseDto[]): TopicArrayResponseDto {
    const response = new TopicArrayResponseDto();
    response.data = data;
    return response;
  }

  toTopicActivitiesResponse(
    data: ActivityTabDto[],
  ): TopicActivitiesResponseDto {
    const response = new TopicActivitiesResponseDto();
    response.data = data;
    return response;
  }
}
