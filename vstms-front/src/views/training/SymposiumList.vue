<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <j-file-upload
        ref="jFileUpload"
        :disabled="false"
        businessType="training_symposium"
        objectId="training_symposium"
        :labelColumn="{ span: 0 }"
        :wrapperColumn="{ span: 24 }"
        :extra="[
          {
            name: '附件',
            emit: 'attach',
          },
        ]"
        @attach="handleAttach"
      />
    </a-card>

    <symposium-attach ref="modalForm"></symposium-attach>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'
import SymposiumAttach from './modules/SymposiumAttach'

export default {
  name: 'SymposiumList',
  components: {
    PageLayout,
    SymposiumAttach,
  },
  data() {
    return {
      title: '座谈会记录',
    }
  },
  methods: {
    handleAttach(attach) {
      this.$refs.modalForm.title = '附件'
      this.$refs.modalForm.object = attach
      this.$refs.modalForm.disableSubmit = true
    },
  },
}
</script>

<style lang="less" scoped>
</style>