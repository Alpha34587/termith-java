# Termith-Java

Termith-java est une chaîne de traitement qui extrait et désambiguise la terminologie de corpus au format tei/xml. A la fin du traitements les fichiers du corpus sont enrichis par différents types d'annotations au format [standOff](https://github.com/laurentromary/stdfSpec) injecter dans les fichiers tei/xml du corpus.
Cette application est développée dans le cadre du projet ISTEX, elle doit être assez performante pour traiter des corpus volumineux (+ de 10000 fichiers).

## Le module d'extraction terminologique

Le module d'extraction terminologique
1. analyse morphosyntaxique et tokenisation à l'aide de treetagger ou de taslimane (l'implémentation de talismane est en cours)
2. extraction terminologique à l'aide de [termsuite](http://termsuite.github.io/) et génération d'une terminologie au format json et tbx 
3. le résultat de cette procédure produits 3 types d'enrichissements standOff par fichier tei/xml du corpus:
    - une tokenisation sur l'éléments `text`
    - l'ajout d'un élément standOff (l'attribut type a pour valeur `wordForms`) qui contient l'ensemble l'analyse morphosyntaxique
    - l'ajout d'un élément standOff (l'attribut type a pour valeur `candidatsTermes`) qui contient l'ensemble de l'extraction terminologique. 

### exemple de fichier de sortie

```xml
<?xml version="1.0" encoding="UTF-8"?>
<TEI xmlns:ns="http://standoff.proposal" xmlns="http://www.tei-c.org/ns/1.0">
  <!-- exemple de teiHeader valide -->
  <teiHeader>
    <fileDesc>
      <titleStmt>
        <title/>
      </titleStmt>
      <publicationStmt>
        <publisher/>
      </publicationStmt>
      <sourceDesc>
        <p/>
      </sourceDesc>
    </fileDesc>
  </teiHeader>
  <!-- un exemple d'annotation morpho-syntaxique -->
  <ns:standOff type="wordForms">
    <teiHeader>
      <fileDesc>
        <titleStmt>
          <title/>
        </titleStmt>
        <publicationStmt>
          <publisher/>
        </publicationStmt>
        <sourceDesc>
          <p/>
        </sourceDesc>
      </fileDesc>
    </teiHeader>
    <ns:listAnnotation>
      <!-- les éléments span correspondent à l'analyse morpho-syntaxique réalisé 
       pour chaque token par la chaîne d'extraction terminologique : 
      cette opération est réalisé au préalable avant la phase d'analyse terminologique. 
      l'attribut target correspond à l'xml:id du token présent dans l'élément text du fichier --> 
      <span target="#t1">
        <fs>
         <!-- l'élément ci-dessous correspond à la forme lemmatisé du token concerné -->
          <f name="lemma">
            <string>un</string>
          </f>
         <!-- l'élément ci-dessous correspond à l'étiquette syntaxique attribuer à ce token -->
          <f name="pos">
            <symbol value="DET:ART"/>
          </f>
        </fs>
      </span>
      <span target="#t2">
        <fs>
          <f name="lemma">
            <string>moulage</string>
          </f>
          <f name="pos">
            <symbol value="NOM"/>
          </f>
        </fs>
      </span>
      <span target="#t3">
        <fs>
          <f name="lemma">
            <string>de</string>
          </f>
          <f name="pos">
            <symbol value="PRP"/>
          </f>
        </fs>
      </span>
      <span target="#t4">
        <fs>
          <f name="lemma">
            <string>le</string>
          </f>
          <f name="pos">
            <symbol value="DET:ART"/>
          </f>
        </fs>
      </span>
      <span target="#t5">
        <fs>
          <f name="lemma">
            <string>stèle</string>
          </f>
          <f name="pos">
            <symbol value="NOM"/>
          </f>
        </fs>
      </span>
      <span target="#t6">
        <fs>
          <f name="lemma">
            <string>figuré</string>
          </f>
          <f name="pos">
            <symbol value="ADJ"/>
          </f>
        </fs>
      </span>
    </ns:listAnnotation>
  </ns:standOff>
  <!-- un exemple d'extraction terminologique -->
  <ns:standOff type="candidatsTermes">
    <teiHeader>
      <fileDesc>
        <titleStmt>
          <title/>
        </titleStmt>
        <publicationStmt>
          <publisher/>
        </publicationStmt>
        <sourceDesc>
          <p/>
        </sourceDesc>
      </fileDesc>
    </teiHeader>
    <ns:listAnnotation>
     <!-- les élément span correspondent au occurrence de terme extraite. 
     L'attribut target correspond au tokens du texte qui compose cette occurrence de terme.
     L'attribut corresp correspond à l'id d'une entrée terminologique présente dans la terminologie 
     généré par la chaîne de traitements. 
     l'attribut ana a pour valeur l'ensemble des annotations lié à ce termes -->
      <span target="#t2" corresp="#TS2.0-entry-5082">
        <fs>
          <f name="inflexionWord">
            <string>moulage</string>
          </f>
        </fs>
      </span>
      <span target="#t5" corresp="#TS2.0-entry-5991">
        <fs>
          <f name="inflexionWord">
            <string>stèle</string>
          </f>
        </fs>
      </span>
    </ns:listAnnotation>
  </ns:standOff>
  <!-- un exemple de contenu de l'élément text tokenisé -->
  <text>
    <front/>
    <body>
      <p>
        <!-- avant tout traitement, le texte de l'élément text du fichier est tokénisé.
        chaque token est englobé par un élément w qui pour attribut un xml:id.
        les valeurs d'attribut sont préfixé par t et ordonné par ordre croissant 
        selon son emplacement dans le texte. 
        Cette nomenclature est utilisé lors de la désambiguisation et doit être respecté. -->
        <w xml:id="t1">Un</w>
        <w xml:id="t2">moulage</w>
        <w xml:id="t3">de</w>
        <w xml:id="t4">la</w>
        <w xml:id="t5">stèle</w>
        <w xml:id="t6">figurée</w>
      </p>
    </body>
    <back/>
  </text>
</TEI
```

## Le module de désambiguisation

1. le module de désambiguisation prend pour entrée des fichiers enrichis par la procédure précedente.
2. il est néccéssaire d'avoir un corpus d'apprentissage qui a été au préalable annoté manuellement.
3. il est néccéssaire d'avoir un corpus d'évaluation.
4. le résultats de cette procédure enrichis les candidat termes de l'éléments standOff (qui a pour type 'candidatsTermes') contenant l'ensemble des candidats termes extraits. l'attribut `ana` des candidats termes pourra prendre 3 valeurs différentes en fonction de la décisions prises par la procédure de désambiguisation :
    - DaOn si le candidat terme est considéré comme terminologique
    - DaOff si le candidat terme est considéré comme non-terminologique
    - noDa si la procédure n'a pris aucune décision

## exemple d'annotations manuelles 
```xml
  <!-- un exemple d'extraction terminologique -->
  <ns:standOff type="candidatsTermes">
    <teiHeader>
      <fileDesc>
        <titleStmt>
          <title/>
        </titleStmt>
        <publicationStmt>
          <publisher/>
        </publicationStmt>
        <sourceDesc>
          <p/>
        </sourceDesc>
      </fileDesc>
    </teiHeader>
    <ns:listAnnotation>
     <!-- les élément span correspondent au occurrence de terme extraite. 
     L'attribut target correspond au tokens du texte qui compose cette occurrence de terme.
     L'attribut corresp correspond à l'id d'une entrée terminologique présente dans la terminologie 
     généré par la chaîne de traitements. 
     l'attribut ana a pour valeur l'ensemble des annotations lié à ce termes -->
           <interpGrp>
        <interp xml:id="DM1">Occurrence du candidat terme validée seulement au niveau syntaxique</interp>
        <interp xml:id="DM2">Occurrence du candidat terme validée au niveau scientifique et syntaxique</interp>
        <interp xml:id="DM3">Occurrence du candidat terme validée au niveau disciplinaire, scientifique et syntaxique</interp>
        <interp xml:id="DM4">Occurrence du candidat terme validée sur tous les niveaux d'annotation. Elle est validée au niveau terminologique</interp>
        <interp xml:id="DM0">Occurrence du candidat terme rejetée dès le niveau syntaxique</interp>
      </interpGrp>
      <interpGrp>
        <interp xml:id="DAOn">Occurrence du candidat terme validée au niveau terminologique selon le système de désambiguïsation utilisé</interp>
        <interp xml:id="DAOff">Occurrence du candidat terme non valide par le système de désambiguïsation</interp>
      </interpGrp>
      <span target="#t2" corresp="#TS2.0-entry-5082" ana="#DM3">
        <fs>
          <f name="inflexionWord">
            <string>moulage</string>
          </f>
        </fs>
      </span>
      <span target="#t5" corresp="#TS2.0-entry-5991" ana="#DM1">
        <fs>
          <f name="inflexionWord">
            <string>stèle</string>
          </f>
        </fs>
      </span>
    </ns:listAnnotation>
  </ns:standOff>
  ```
## exemple d'annotations automatiques

```xml
  <!-- un exemple d'extraction terminologique -->
  <ns:standOff type="candidatsTermes">
    <teiHeader>
      <fileDesc>
        <titleStmt>
          <title/>
        </titleStmt>
        <publicationStmt>
          <publisher/>
        </publicationStmt>
        <sourceDesc>
          <p/>
        </sourceDesc>
      </fileDesc>
    </teiHeader>
    <ns:listAnnotation>
               <interpGrp>
        <interp xml:id="DM1">Occurrence du candidat terme validée seulement au niveau syntaxique</interp>
        <interp xml:id="DM2">Occurrence du candidat terme validée au niveau scientifique et syntaxique</interp>
        <interp xml:id="DM3">Occurrence du candidat terme validée au niveau disciplinaire, scientifique et syntaxique</interp>
        <interp xml:id="DM4">Occurrence du candidat terme validée sur tous les niveaux d'annotation. Elle est validée au niveau terminologique</interp>
        <interp xml:id="DM0">Occurrence du candidat terme rejetée dès le niveau syntaxique</interp>
      </interpGrp>
      <interpGrp>
        <interp xml:id="DAOn">Occurrence du candidat terme validée au niveau terminologique selon le système de désambiguïsation utilisé</interp>
        <interp xml:id="DAOff">Occurrence du candidat terme non valide par le système de désambiguïsation</interp>
      </interpGrp>
     <!-- les élément span correspondent au occurrence de terme extraite. 
     L'attribut target correspond au tokens du texte qui compose cette occurrence de terme.
     L'attribut corresp correspond à l'id d'une entrée terminologique présente dans la terminologie 
     généré par la chaîne de traitements. 
     l'attribut ana a pour valeur l'ensemble des annotations lié à ce termes -->
      <span target="#t2" corresp="#TS2.0-entry-5082" ana="#DM3 #DAOff">
        <fs>
          <f name="inflexionWord">
            <string>moulage</string>
          </f>
        </fs>
      </span>
      <span target="#t5" corresp="#TS2.0-entry-5991" ana ="#DM1 #DAOn">
        <fs>
          <f name="inflexionWord">
            <string>stèle</string>
          </f>
        </fs>
      </span>
    </ns:listAnnotation>
  </ns:standOff>
  ```
## Applications externes requises

- [R](https://www.r-project.org/)
- [TreeTagger](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/)

## Installation

1. s'assurer que TreeTagger, R et java 8 soient bien installés
2. télécharger la dernière version de termith-java disponible [ici]( https://github.com/simonmeoni/termith-java/releases)
3. installer les librairie R suivantes : `data.table` & `Rserve`
4. pour installer `Rserve` :
    1. ouvrir un terminal et lancer la commande `R`
    2. dans l'interpréteur de commande R, taper la commande : `install.packages("Rserve")` et suivre les instructions
    3. taper ensuite la commmande `library('Rserve')` pour charger la librairie précédemment installé
    4. taper ensuite `Rserve()` pour lancer le serveur R. un sous-processus est lancé en tâche de fond, le terminal peut être fermé.
5. pour installer `data.table`, il faut lancer une instance de R (comme ci-dessus) et lancer la commande `install.packages('data.table')`

## Comment utiliser Termith-Java ?

1. pour le module d'extraction terminologique est appelé avec la commande suivante :
`termITH-all-v0.4-SNAPSHOT.jar org.atilf.cli.TermithTreeTaggerCLI`
`-i /chemin/vers/corpus/tei`
`-o /chemin/vers/sortie`
`-l en`
`-tt chemin/vers/dossier/installation/de/treetagger`
      * l'option `-i` correspond au corpus d'entrée donné au programme
      * l'option `-o` correspond au chemin où seront écrits les résultats
      * l'option `-l` à la langue du corpus d'entrée
      * l'option `-tt` au chemin du **dossier d'installation** de treetagger  

2. Le module de désambiguisation est appelé avec la commande suivante :
`org.atilf.cli.DisambiguationCLI`
`-le /home/smeoni/Documents/desamb/res/learning-test`
`-e /home/smeoni/Documents/desamb/res/evaluation`
`-o /home/smeoni/Documents/test/archeo-desamb`
      * l'option `-le` correspond au corpus d'apprentissage dont les termes sont annotés manuellement
      * l'option `-e` correspond au corpus d'évaluation à désambiguiser
      * l'option `-o` correspond chemin où seront écrit les résultats du corpus d'évaluation désambiguisé

## Liens Utiles

- http://www.atilf.fr/ressources/termith/
- https://github.com/termsuite
- http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/
- https://www.r-project.org/
- http://www.tei-c.org/index.xml

## Contact
- simonmeoni@aol.com / simon.meoni@atilf.fr
