import {Account} from '@/model/account/Account';
import { BlogState } from './BlogState';
import { Blog } from '@/model/Blog';
import { AccountEnum } from '../account/AccountEnum';

export class RootState {
    public path: string; // 当前的窗口路径
    public account: Account|null; // 当前账号对象
    // 视图的状态
    public blogState: BlogState;

    public constructor() {
        this.path = '/';
        this.account = {
            name: null,
            password: null,
            token: null,
            role: AccountEnum.ROLE_VISTOR,
        };
        this.blogState = new BlogState();
        // test
        const blog: Blog = {
            id: '1',
            title: 'The first Blog',
            content: 'This reports custom components which don\'t'
            + 'have v-bind:key. One is .... Elements in ',
            authorId: '',
            authorName: '',
            date: 0,
        };
        this.blogState.blog = blog;
        this.blogState.blogList.push(blog);
    }
}
