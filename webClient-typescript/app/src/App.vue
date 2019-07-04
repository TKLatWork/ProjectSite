<template>
  <div id="App">
    <NavHeader />
    <div id="Content"><router-view /></div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import NavHeader from '@/components/NavHeader.vue'; // @ is an alias to /src

@Component({
  components: {
    NavHeader,
  },
})
export default class App extends Vue {}
</script>

<style lang="scss">
#Content{
  margin-top: 40px;
  margin-left: 180px;
  padding: 2px;
}
</style>
