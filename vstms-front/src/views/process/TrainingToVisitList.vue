<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="培训班" required>
                <j-search-select-tag
                  ref="selectTag"
                  v-model="queryParam.classNo"
                  :triggerChange="true"
                  placeholder="请选择培训班"
                  dict="training_class,name,no"
                  :disabled="false"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" icon="search">查询</a-button>
                <a-button type="primary" @click="() => (queryParam = {})" icon="reload" style="margin-left: 8px"
                  >重置</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 内容区域 -->
      <div v-if="queryParam.classNo">
        <j-file-upload
          ref="jFileUpload"
          :disabled="false"
          businessType="training_to_visit"
          :objectId="queryParam.classNo"
          text="上传外出参观方案/记录"
          :labelColumn="{ span: 0 }"
          :wrapperColumn="{ span: 24 }"
          :extra="[
            {
              name: '外出参观详情',
              emit: 'attach',
            },
          ]"
          @attach="handleAttach"
        />
      </div>

      <a-empty v-else description="请先选择一个培训班，再进行操作" />

      <!-- 模态框 -->
      <training-to-visit-attach ref="modalForm"></training-to-visit-attach>
    </a-card>
  </page-layout>
</template>

<script>
import PageLayout from '@/components/page/PageLayout'
import TrainingToVisitAttach from './modules/TrainingToVisitAttach'

export default {
  name: 'TrainingToVisitList',
  components: {
    PageLayout,
    TrainingToVisitAttach,
  },
  data() {
    return {
      title: '外出参观 方案/记录',
      queryParam: {},
    }
  },
  methods: {
    handleAttach(attach) {
      this.$refs.modalForm.title = '外出参观详情附件'
      this.$refs.modalForm.object = attach
      this.$refs.modalForm.disableSubmit = true
    },
  },
}
</script>

<style lang="less" scoped>
</style>