package controllers

import javax.inject.Inject

import controllers.auth.AuthenticationModule
import elastic.ElasticClient
import models.{CerebroResponse, ElasticServer, IndexMetadata}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global

class CreateIndexController @Inject()(val authentication: AuthenticationModule,
                                      client: ElasticClient) extends BaseController {

  def execute = process { request =>
    client.createIndex(
      request.get("index"), request.getObjOpt("metadata").getOrElse(Json.obj()),
      request.target
    ).map { response =>
      CerebroResponse(response.status, response.body)
    }
  }

  def getIndexMetadata = process { request =>
    client.getIndexMetadata(request.get("index"), request.target).map { response =>
      CerebroResponse(response.status, IndexMetadata(response.body))
    }
  }

}
