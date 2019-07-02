import {Account} from '@/model/Account';
import { BlogState } from './BlogState';

export class RootState {
    public path: string; // 当前的窗口路径
    public act: Account|null; // 当前账号对象
    // 视图的状态
    public blog: BlogState;

    public constructor() {
        this.path = '/';
        this.act = null;
        this.blog = new BlogState();
    }
}
