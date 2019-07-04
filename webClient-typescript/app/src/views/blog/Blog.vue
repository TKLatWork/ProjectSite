<!-- Blog 的首页，也是Blog的List页-->
<template>
  <div class="blog">
    <BlogHeader />
    <!-- blog 记录的Card展示和分页页面 -->
    <div class="block">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
    <!-- Blog Card列表-->
    <el-collapse>
      <el-collapse-item v-bind:key="blogItem.id" :title="blogItem.title" v-for="blogItem in blogState.blogList">
        <router-link :to="{path:'/blog/view/' + blogItem.id}">{{blogItem.id}}</router-link>
        <div>{{toBrief(blogItem.content)}}</div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>


<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { State, Action, Getter } from 'vuex-class';
import BlogHeader from '@/components/blog/BlogHeader.vue';
import { BlogState } from '../../model/state/BlogState';


@Component({
  components: {
    BlogHeader,
  },
})
export default class Blog extends Vue {

  public total: number = 0;
  public pageSize: number = 0;
  public currentPage: number = 0;
  @State private account!: Account;
  @State private blogState!: BlogState;

  public handleCurrentChange(val: number) {
    console.log(`当前页: ${val}`);
    // TODO 实现翻页的数据加载
  }

  public toBrief(text: string): string {
    if ( text.length > 40 ) {
        return text.substring(0, 200) + '...';
      } else {
        return text;
      }
  }

}
</script>