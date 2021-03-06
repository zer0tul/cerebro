package controllers

import javax.inject.Inject

import controllers.auth.AuthenticationModule
import elastic.ElasticClient
import models.{CerebroResponse, ElasticServer}
import models.repository.Repositories

import scala.concurrent.ExecutionContext.Implicits.global

class RepositoriesController @Inject()(val authentication: AuthenticationModule,
                                       client: ElasticClient) extends BaseController {

  def get = process { request =>
    client.getRepositories(request.target).map { response =>
      CerebroResponse(response.status, Repositories(response.body))
    }
  }

  def save = process { request =>
    val name = request.get("name")
    val repoType = request.get("type")
    val settings = request.getObj("settings")
    client.createRepository(name, repoType, settings, request.target).map {
      response => CerebroResponse(response.status, response.body)
    }
  }

  def delete = process { request =>
    val name = request.get("name")
    client.deleteRepository(name, request.target).map { response =>
      CerebroResponse(response.status, response.body)
    }
  }

}
