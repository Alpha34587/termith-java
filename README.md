# Termith-Java

Termith-java est une chaîne de traitement qui extrait et désambiguise la terminologie de corpus au format tei/xml. A la fin du traitements les fichiers du corpus sont enrichis par différents types d'annotations au format standOff.
Cette application est développée dans le cadre du projet ISTEX, elle doit être assez performante pour traiter des corpus volumineux (+ de 10000 fichiers).

## Les enrichissements

- analyse morphosyntaxique et tokenisation à l'aide de treetagger ou de taslimane (l'implémentation de talismane est en cours)
- analyse terminologique à l'aide de [termsuite](http://termsuite.github.io/)
- désambiguisation terminologique

## Applications requises

- [R](https://www.r-project.org/)
- [TreeTagger](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/)

## Installation

1. s'assurer que TreeTagger et R soient bien installés
2. télécharger la dernière version de termith-java disponible [ici]( https://github.com/simonmeoni/termith-java/releases)
3. installer les librairie R suivantes : `data.table` & `Rserve`
4. démarrer un serveur Rserve

## Comment utiliser Termith-Java ?

Il y a deux types d'analyses possibles :
1. Une analyse morphosyntaxique et terminologique qui est appélé avec la commande suivante :
`termITH-all-v0.4-SNAPSHOT.jar org.atilf.cli.TermithTreeTaggerCLI`
`-i /chemin/vers/corpus/tei`
`-o /chemin/vers/sortie`
`-l en`
`-tt chemin/vers/dossier/installation/de/treetagger`
  * l'option `-i` correspond au corpus d'entrée donné au programme
  * l'option `-o` correspond au chemin où seront écrits les résultats
  * l'option `-l` à la langue du corpus d'entrée
  * l'option `-tt` au chemin du **dossier d'installation** de treetagger  

2. La désambiguisation des occurrences de termes des fichiers d'un corpus à l'aide d'une méthode probabiliste :
`org.atilf.cli.DisambiguationCLI`
`-le /home/smeoni/Documents/desamb/res/learning-test`
`-e /home/smeoni/Documents/desamb/res/evaluation`
`-o /home/smeoni/Documents/test/archeo-desamb`
  * l'option `-le` correspond au corpus d'apprentissage dont les termes sont annotés manuellement
  * l'option `-e` correspond au corpus d'évaluation à désambiguiser
  * l'option `-o` correspond chemin où seront écrit les résultats du corpus d'évaluation désambiguisé

## Le format d'entrée

Les fichiers du corpus doivent être au format tei et conforme à ce standard.
En ce qui concerne la désambiguisation, les fichiers tei doivent contenir des annotations standOff concernant la morphosyntaxe et la terminologie. Les fichiers doivent être conforme au schéma standOff/tei qui se trouve [ici](https://github.com/laurentromary/stdfSpec/blob/AnnArbor/Schemas/standoff-proposal.rnc).   
Voici un exemple de ces annotations et du fulltext tokénisé ci-dessous.

### Annotation morphosyntaxique

### Annotation terminologique

## Annotations de la désambiguisation

## Liens Utiles

- http://www.atilf.fr/ressources/termith/
- https://github.com/termsuite
- http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/
- https://www.r-project.org/
- http://www.tei-c.org/index.xml
