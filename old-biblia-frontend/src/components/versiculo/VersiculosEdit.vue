<template>
  <div>
    <div id="myModal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <!-- <h4 class="modal-title">Expressão: '{{ expressaoSelecionada.text }}: {{ expressaoSelecionada.start }} - {{ expressaoSelecionada.end}}'</h4> -->
            <h4 class="modal-title">Expressão: {{ titleExpressao }}</h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <form>
                <div class="form-group row">
                  <label for="exampleTextarea" class="col-form-label col-sm-2 col-md-2 col-lg-2 col-xs-2">Descrição</label>
                  <textarea class="col-sm-8 col-lg-8 col-xs-8 col-md-8" id="exampleTextarea" rows="3"></textarea>
                </div>
                <div class="form-group row">
                  <div class="col-sm-3">
                    <input type="text" class="form-control">
                  </div>
                  <div class="col-sm-2">
                    <button class="btn btn-primary" type="button" name="button">Adicionar</button>
                  </div>
                </div>
                <div class="form-group row">
                  <div class="col-sm-3">
                    <select class="form-control" name="">
                      <option value="GREGO">GREGO</option>
                      <option value="HEBRAICO">HEBRAICO</option>
                      <option value="ARAMAICO">ARAMAICO</option>
                    </select>
                  </div>
                </div>
              </form>

              <table id="datatable_id" class="table table-responsive table-bordered table-bordered">
                <thead>
                  <tr>
                    <td>CÓDIGO</td>
                    <td>IDIOMA</td>
                    <td>AÇÕES</td>
                  </tr>
                </thead>
              </table>
            </div>
          </div>

          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <div style="position:relative" class="editing">
      <div>
        <ul class="list-group">
          <li class="list-group-item" v-for="versiculo in versiculos">
            <div :json="versiculo.key.json" v-html="versiculo.versiculoMontado" class="versiculo"></div>
          </li>
        </ul>

        <ul class="list-group lista_limpa">
          <li class="list-group-item" v-for="versiculo in versiculos">
            <div :json="versiculo.key.json" class="versiculo" v-text="versiculo.texto"></div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<style lang="scss">
.editing {
  .list-group {
    &.lista_limpa {
      opacity: 0.3;
    }
    .list-group-item {
      .versiculo {
        display: inline;
      }
    }
  }
  .texto {
    text-decoration: underline;
  }
}
</style>
<script>
export default {
  name: 'versiculos-edit',
  props: ['versiculos'],
  data: () => ({
    expressaoSelecionada: {
      text: '',
      start: null,
      end: null
    },
    dt: null
  }),
  computed: {
    titleExpressao() {
      let e = this.expressaoSelecionada
      // '{{ expressaoSelecionada.text }}: {{ expressaoSelecionada.start }} - {{ expressaoSelecionada.end}}'
      return `'${e.text}': ${e.start} - ${e.end}`
    }
  },
  methods: {
    createTable(_this) {
      if ( !$.fn.DataTable.isDataTable( '#datatable_id' ) ) {
        let options = {
          "paging":   false,
          "ordering": false,
          "info":     false,
          "dom": 't',
          "language": {
            "emptyTable": "Nao há expressões"
          },
          columns: [
            {data: 'codigo'},
            {data: 'idioma'},
            {data: 'acoes', defaultContent: ''}
          ]
        }
        _this.dt = $("#datatable_id").DataTable(options)
        _this.dt.draw()
      }
    },
    carregarDadosModal (_this, key) {
      _this.createTable(_this)

      let versiculo = _this.versiculos.filter(v => {
        return v.numero == key.id
      })[0]

      let expressao = versiculo.expressoes.filter(e => {
        if (e.inicio === _this.expressaoSelecionada.start &&
          e.fim    === _this.expressaoSelecionada.end)
          return e
        })[0]

        _this.dt.clear()
        if (expressao != undefined) { // popula as tabelas
          expressao.dicionarios.forEach(d => {
            _this.dt.row.add({
              'codigo': d.key.id,
              'idioma': d.key.idioma
            })
          })
        }
        _this.dt.draw()

      },
      bindVersiculoSelect (_this) {
        $(".list-group").css("position", "absolute");


        $(".lista_limpa .versiculo").off("mouseup").on("mouseup", function() {

          var expressao = $(this).textSelected();
          var keyAsJson = $(this).attr("json");

          if ( expressao != undefined && expressao != null && expressao.text != "") {
            console.log( expressao.text + " - " + keyAsJson );
            _this.expressaoSelecionada = expressao;

            window.jQuery('#myModal').off('show.bs.modal').on('show.bs.modal', e => {
              _this.carregarDadosModal(_this, JSON.parse(keyAsJson))
            })

            window.jQuery('#myModal').modal('show')
          }

        });
      }
    },
    mounted: function() {
      this.bindVersiculoSelect(this)
    },
    updated: function() {
      this.bindVersiculoSelect(this)
    }
  }
  </script>
