import { Blog } from '@/model/Blog';

export class BlogState {
    public blog: Blog|null;

    public constructor() {
        this.blog = null;
    }
}
