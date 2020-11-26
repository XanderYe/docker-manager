<template>
  <div>
    <div style="padding: 10px">
      <Button :disabled="editDisabled" @click="openConfigModal" style="width: 120px">编辑</Button>
      <Dropdown trigger="click" @on-click="controlContainer">
        <Button type="primary" :disabled="selectDisabled" style="width: 120px">
          操作
          <Icon type="ios-arrow-down"></Icon>
        </Button>
        <DropdownMenu slot="list" style="width: 120px">
          <DropdownItem v-for="option in optionList" :key="option.id" :name="option.id" :disabled="option.disabled">{{option.name}}</DropdownItem>
        </DropdownMenu>
      </Dropdown>

    </div>
    <Table highlight-row ref="containerTable" :columns="columns" :data="containerList" @on-current-change="selectContainer">
    </Table>

    <Modal id="configModal" title="修改配置" v-model="configModal" :mask-closable="false" width="50%">
      <Tabs value="mount" v-if="container">
        <TabPane label="卷" name="mount">
          <div>
            <Button type="primary" icon="md-add" @click="addMount"></Button>
          </div>
          <div class="table">
            <div class="flex header">
              <div class="column">文件/文件夹</div>
              <div class="column">装载路径</div>
              <div class="half-column">只读</div>
              <div class="half-column">操作</div>
            </div>
            <div class="flex body" v-for="(volume, index) in container.mountPointList" :key="index">
              <div class="column"><Input v-model="volume.source"/></div>
              <div class="column"><Input v-model="volume.target"/></div>
              <div class="half-column"><Checkbox size="large"></Checkbox></div>
              <div class="half-column"><Button type="error" size="small" icon="md-remove" @click="removeMount(index)"></Button></div>
            </div>
          </div>
        </TabPane>
        <TabPane label="端口" name="port">
          端口配置
        </TabPane>
        <TabPane label="环境" name="env">
          环境变量配置
        </TabPane>
      </Tabs>
    </Modal>
  </div>
</template>

<script>
  export default {
    name: "Index",
    data() {
      return {
        configModal: false,
        editDisabled: true,
        selectDisabled: true,
        container: null,
        optionList: [
          {
            id: 1,
            name: "启动",
            disabled: true
          },
          {
            id: 2,
            name: "停止",
            disabled: true
          },
          {
            id: 3,
            name: "重新启动",
            disabled: false
          }
        ],
        containerList: [],
        columns: [
          {
            title: "名称",
            key: "name",
            render: (h, params) => {
              const row = params.row;
              const name = row.name;
              const image = row.image;
              return h("div", [
                h('div', name),
                h('div', image)
              ]);
            }
          },
          {
            title: "状态",
            key: "state",
            render: (h, params) => {
              const row = params.row;
              const state = row.state;
              const startTime = row.startTime;
              let text;
              if (state === "运行中" && startTime) {
                let time = new Date(startTime);
                let now = new Date();
                let last = now.getTime() - time.getTime();
                text = this.parseTime(last);
              }
              return h("div", [
                h('div', state),
                h('div', text)
              ]);
            }
          },
          {
            title: "操作",
            key: "operate",
            render: (h, params) => {
              return h('div', [
                h('i-switch', {
                  props: {
                    value: params.row.state === "运行中"
                  },
                  on: {
                    'on-change': (value) => {
                      this.switchContainer(params.row, value)
                    }
                  }
                })
              ])
            }
          }
        ]
      }
    },
    methods: {
      getContainerStatusList() {
        this.$requests.get("/container/getContainerStatusList", {}).then(res => {
          if (res.data.code === 0) {
            this.containerList = res.data.data;
            if (this.container != null) {
              this.containerList.map(ctn => {
                if (ctn.id === this.container.id) {
                  ctn._highlight = true;
                }
                return ctn;
              })
            }
          }
        });
      },
      getContainerConfig() {
        if (this.container) {
          this.$requests.get("/container/getContainerConfig", {id: this.container.id}).then(res => {
            if (res.data.code === 0) {
              this.container = res.data.data;
            }
          });
        }
      },
      selectContainer(currentRow) {
        this.container = currentRow;
        this.selectDisabled = false;
        switch (this.container.state) {
          case "运行中": {
            this.optionList[0].disabled = true;
            this.optionList[1].disabled = false;
            this.optionList[2].disabled = false;
            this.editDisabled = true;
          }
            break;
          case "已停止": {
            this.optionList[0].disabled = false;
            this.optionList[1].disabled = true;
            this.optionList[2].disabled = true;
            this.editDisabled = false;
          }
          break;
          default: {
            this.optionList[0].disabled = true;
            this.optionList[1].disabled = true;
            this.optionList[2].disabled = true;
            this.editDisabled = true;
          }
          break;
        }
      },
      controlContainer(value) {
        switch (value) {
          case 1:
            this.startContainer(this.container.id);
            break;
          case 2:
            this.stopContainer(this.container.id);
            break;
          case 3:
            this.restartContainer(this.container.id);
            break;
        }
      },
      switchContainer(container, value) {
        if (value) {
          this.startContainer(container.id);
        } else {
          this.stopContainer(container.id);
        }
      },
      startContainer(id) {
        this.$requests.post("/container/start", {id: id}).then(res => {
          if (res.data.code === 0) {
            this.$Message.success("启动成功");
            this.getContainerStatusList();
            this.editDisabled = true;
          } else {
            this.$Message.error(res.data.msg);
          }
        })
      },
      stopContainer(id) {
        this.$requests.post("/container/stop", {id: id}).then(res => {
          if (res.data.code === 0) {
            this.$Message.success("停止成功");
            this.getContainerStatusList();
            this.editDisabled = false;
          } else {
            this.$Message.error(res.data.msg);
          }
        })
      },
      restartContainer(id) {
        this.$requests.post("/container/restart", {id: id}).then(res => {
          if (res.data.code === 0) {
            this.$Message.success("重启成功");
            this.getContainerStatusList();
          } else {
            this.$Message.error(res.data.msg);
          }
        })
      },
      openConfigModal() {
        this.configModal = true;
        this.getContainerConfig();
      },
      addMount() {
        if (this.container) {
          this.container.mountPointList.push({
            source: "",
            target: ""
          })
        }
      },
      removeMount(index) {
        if (this.container && index < this.container.mountPointList.length) {
          this.container.mountPointList.splice(index, 1);
        }
      },
      parseTime(last) {
        let text = "已运行 ";
        const mins = 60 * 1000;
        const hours = 60 * mins;
        const days = 24 * hours;
        if (last < mins) {
          text += " 1 分钟";
        } else if (last < hours) {
          text += parseInt(last / mins) + " 分钟";
        } else if (last < days) {
          text += parseInt(last / hours) + " 小时";
        } else {
          text += parseInt(last / days) + " 天";
        }
        return text;
      }
    },
    created() {
      this.getContainerStatusList();
      var interval = setInterval(() => {
        this.getContainerStatusList();
      }, 3000)
    }
  }
</script>

<style lang="less">
  #configModal {

    .ivu-modal-body {
      height: 500px;
    }
    .table {
      width: 100%;
      overflow-y: auto;
      height: 360px;
      margin-top: 10px;

      &::-webkit-scrollbar {
        width: 9px;
        height: 9px;
      }

      &::-webkit-scrollbar-track {
        border-radius: 8px;
        background-color: #ffffff;
      }

      &::-webkit-scrollbar-thumb {
        border-radius: 8px;
        background-color: #d6d6d6;
      }

      .flex {
        display: flex;
      }
      .header {
        width: 100%;
        font-weight: bold;
        .column {
          width: 31%;
          position: relative;
          margin-right: 10px;
        }
        .half-column {
          width: 15%;
          position: relative;
          margin-right: 10px;
          text-align: center;
        }
      }
      .body {
        width: 100%;
        margin-top: 10px;
        .column {
          width: 31%;
          position: relative;
          margin: 0 10px 10px 0;
        }
        .half-column {
          width: 15%;
          position: relative;
          margin-right: 10px;
          display: flex;
          align-items:center;
          justify-content:center;
        }
      }
    }
  }
</style>
