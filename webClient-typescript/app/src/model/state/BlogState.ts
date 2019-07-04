import { Blog } from '@/model/Blog';

export class BlogState {
    public blog: Blog|null = null;
    public blogList: Blog[] = [];
}
