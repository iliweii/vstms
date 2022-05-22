<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="declare_tender"
        objectId="declare_tender"
        dict="file_declare_tender"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
      />
    </a-card>
  </page-layout>
</template>

<script>
  import PageLayout from '@/components/page/PageLayout'

  export default {
    name: 'TenderFile',
    components: {
      PageLayout
    },
    data () {
      return {
        title: '招标投标文件'
      }
    }
  }
</script>

<style lang="less" scoped>
</style>