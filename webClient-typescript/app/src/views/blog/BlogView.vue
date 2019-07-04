<!-- Blog 的详细页-->
<template>
  <div class="blog">
    <BlogHeader />
    <!-- blog 标题内容 -->
    <div v-if="blog">
      <div class="blogTitle">{{blog.title}}</div>
      <div>By {{blog.authorName}} @{{util().toDate(blog.date)}}</div>
      <div id="editor"></div>
    </div>
  </div>
</template>


<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import { State, Action, Getter } from 'vuex-class';
import BlogHeader from '@/components/blog/BlogHeader.vue';
import { Blog } from '@/model/Blog';
import { BlogState } from '@/model/state/BlogState';
import DataUtil from '@/util/DataUtil';
import Jodit from 'Jodit';

var editor = new Jodit('#editor');


@Component({
  components: {
    BlogHeader,
  },
})
export default class BlogView extends Vue {

  public config = {};

  @Prop(String) private id!: string;
  @State private account!: Account;
  @State private blogState!: BlogState;

  private blog: Blog|null = null;

  public mounted() {
    if (this.blogState) {
      this.blog = this.blogState.blog;
    }
  }

  public util(): DataUtil {
    return DataUtil;
  }

}
</script>

<style lang="scss">
.blogTitle{
  text-align: center;
  font-size: large;
}
</style>