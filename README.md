 # Termith-Java

Termith-java est une application java d'analyse de corpus au format tei. La batterie d'analyse effectuée par le programme est reporté dans des balises au format standOff. Les analyses proposées sont focalisées sur le fulltext du format tei. Cette application est développée dans le cadre du projet ISTEX, elle doit être assez performante afin de traiter des corpus volumineux.

## Les Enrichissements

- analyse morphosyntaxique à l'aide de treetagger ou de taslimane (à venir)
- analyse terminologique à l'aide de termsuite
- désambiguisation de terminologie
- extraction de phraséologies complexes (à venir)

## Applications requises

- [R](https://www.r-project.org/)
- [TreeTagger](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/)

## Installation

1. s'assurer que TreeTagger et R soient bien installés
2. télécharger la dernière version de termith-java disponible [ici]( https://github.com/simonmeoni/termith-java/releases)

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

2. La désambiguisation des occurences de termes des fichiers d'un corpus à l'aide d'une méthode probabiliste :
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
