#include <stdio.h>
#include <stdlib.h>


void bubble(int *v, int len){
	int i, j, aux;

	for (i = 0; i < len; i++){
		for (j = 0; j < len - 1 - i; j++){
			if (v[j] > v[j+1]){
				aux = v[j];
				v[j] = v[j+1];
				v[j+1] = aux;
			}
		}
	}
}



int main(){
	int n, *vec, i;

	printf("Number of elements:\n");

	scanf("%d", &n);
	
	vec = (int*)malloc(n * sizeof(int));

	printf("Insert values:\n");

	for(i = 0; i < n; i++){
		scanf("%d", vec + i);
	}

	bubble(vec, n);

	printf("Sorted:\n");

	for(i = 0; i < n; i++){
		printf("%d\n", vec[i]);
	}

	return 0;
}
