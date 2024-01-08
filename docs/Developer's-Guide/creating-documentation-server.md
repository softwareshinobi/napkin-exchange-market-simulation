# MkDocs w/ Material w/ Blog

Here's a basic MkDocs configuration for using the Material theme with the blog plugin:

## 1. Install the necessary plugins

```bash
pip install mkdocs-material mkdocs-blog-plugin
```

## 2. Create a new MkDocs project

```bash
mkdocs new my-project

cd my-project
```

## 3. Edit the mkdocs.yml file

```yaml
site_name: My Blog

theme:
  name: material

plugins:
  - search
  - blog:
      paginate: 10  # Number of posts per page
      sort_by: date  # Sort blog posts by date

markdown_extensions:
  - meta
  - toc:
      permalink: true

extra_javascript:
  - https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-MML-AM_CHTML
```

## 4. Create your content

`Blog posts`: Place them in the docs/blog directory.

`Other pages`: Place them in the docs directory.

## Build and serve the site

```bash
mkdocs serve
```
