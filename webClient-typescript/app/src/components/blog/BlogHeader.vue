<template>
  <div>
    <router-link to="/blog">Blog首页</router-link> |
    <router-link to="/blog/edit">New！</router-link> |
    <el-input class="normalBtn" placeholder="Blog ID" v-model="goBlogId" clearable></el-input>
    <router-link :to="{path:'/blog/view/' + goBlogId}">GO</router-link>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class BlogHeader extends Vue {
  public goBlogId: string = '';
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

.normalBtn{
  width: 200px;
}

</style>
