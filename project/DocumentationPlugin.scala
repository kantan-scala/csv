/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.typesafe.sbt.site.SitePlugin
import com.typesafe.sbt.site.SitePlugin.autoImport.makeSite
import com.typesafe.sbt.site.SitePlugin.autoImport.siteSubdirName
import com.typesafe.sbt.site.preprocess.PreprocessPlugin
import com.typesafe.sbt.site.preprocess.PreprocessPlugin.autoImport.*
import com.typesafe.sbt.site.util.SiteHelpers.*
import mdoc.MdocPlugin
import mdoc.MdocPlugin.autoImport.*
import sbt.*
import sbt.Keys.*
import sbt.ScopeFilter.ProjectFilter

/** Plugin for documentation projects.
  *
  * Enabling this will set things up so that:
  *   - `makeSite` compiles all mdoc files and builds a complete documentation site.
  */
object DocumentationPlugin extends AutoPlugin {

  override def trigger =
    noTrigger

  override def requires: Plugins =
    PreprocessPlugin && MdocPlugin

  object autoImport {
    def kantanCsvVersion = "0.10.0"

    /** This is mostly meant as an internal setting, initialised if `scmInfo` is set. But you can override it. */
    val docSourceUrl: SettingKey[Option[String]] = settingKey("scalac -doc-source-url parameter")

    def inProjectsIf(predicate: Boolean)(projects: ProjectReference*): ProjectFilter =
      if(predicate) inProjects(projects*)
      else inProjects()
    val mdocSite: TaskKey[Seq[(File, String)]] =
      taskKey[Seq[(File, String)]]("create mdoc documentation in a way that lets sbt-site grab it")
    val mdocSiteOut: SettingKey[String] =
      settingKey[String]("name of the directory in which sbt-site will store mdoc documentation")
  }

  import autoImport.*

  override def projectSettings: Seq[Setting[?]] =
    scaladocSettings ++ mdocSettings ++ siteSettings

  def siteSettings: Seq[Setting[?]] =
    Seq(
      SitePlugin.autoImport.makeSite / includeFilter :=
        "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.eot" | "*.svg" | "*.ttf" |
          "*.woff" | "*.woff2" | "*.otf",
      // Configures task dependencies: doc → makeSite → mdoc
      makeSite := makeSite.dependsOn(mdocSite).value,
      // Use a "managed" source directory for preprocessing - we want all documentation to be preprocessed, and the only
      // way I found to achieve that is to have all md files to be copied / generated to the same directory, and *then*
      // preprocess that.
      Preprocess / sourceDirectory := resourceManaged.value / "main" / "site-preprocess"
    )

  def mdocSettings: Seq[Setting[?]] =
    Seq(
      mdocSite := {
        val out = mdocOut.value
        for {
          (file, name) <- (out ** AllPassFilter --- out).pair(Path.relativeTo(out))
        } yield file -> name
      },
      mdocSite := mdocSite.dependsOn(mdoc.toTask(" ")).value,
      mdocExtraArguments += "--no-link-hygiene",
      mdocSiteOut := "./",
      mdocIn := (Compile / sourceDirectory).value / "mdoc",
      mdocVariables := Map(
        "VERSION" -> kantanCsvVersion
      ),
      addMappingsToSiteDir(mdocSite, mdocSiteOut)
    )

  def scaladocSettings: Seq[Setting[?]] =
    Seq(
      docSourceUrl := scmInfo.value.map(i => s"${i.browseUrl}/tree/master€{FILE_PATH}.scala")
    )
}
